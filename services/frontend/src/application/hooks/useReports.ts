import { useState, useEffect, useCallback } from 'react';
import { type Report } from '../../domain/models/Report';
import { ReportService } from '../services/ReportService';
import { FileService } from '../services/FileService';
import { useAuthContext } from '../../presentation/context/AuthContext';

export const useReports = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const { user } = useAuthContext();

  const fetchReports = useCallback(async (showLoading = true) => {
    if (showLoading) setIsLoading(true);
    setError(null);
    try {
      const data = await ReportService.getAll();
      setReports(data);
    } catch (err) {
      setError('Failed to fetch reports');
    } finally {
      if (showLoading) setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchReports(true); // Initial fetch

    // Poll every 1 minute to update PROCESSING status
    const intervalId = setInterval(() => {
      fetchReports(false); // Background fetch without triggering full loader
    }, 60000);

    return () => clearInterval(intervalId);
  }, [fetchReports]);

  const createReport = async (title: string, clientId: string, audioFile: File) => {
    // 🛡️ SOLUÇÃO AQUI: Garantindo que o user não é nulo para o TypeScript
    if (!user) {
      setError('User is not authenticated');
      throw new Error('User is not authenticated');
    }

    setIsLoading(true);
    try {
      // 1. Get Pre-Signed URL
      const { uploadUrl, fileKey } = await FileService.getPreSignedUrl(audioFile.name, audioFile.type);
      
      // 2. Upload to S3
      await FileService.uploadToS3(uploadUrl, audioFile);

      // 3. Create Report with fileKey
      // @ts-ignore - Caso o FileService atual não exporte o getFileUrlFromUploadUrl
      const audioUrl = FileService.getFileUrlFromUploadUrl ? FileService.getFileUrlFromUploadUrl(uploadUrl) : '';
      
      const newReport = await ReportService.create({ 
        title, 
        clientId, 
        audioFileKey: fileKey, 
        audioUrl, 
        userId: user.userId, 
        userName: user.name 
      });
      
      setReports((prev) => [...prev, newReport]);
      return newReport;
    } catch (err) {
      setError('Failed to create report and upload file');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteReport = async (id: string) => {
    setIsLoading(true);
    try {
      await ReportService.delete(id);
      setReports((prev) => prev.filter((r) => r.id !== id));
    } catch (err) {
      setError('Failed to delete report');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    reports,
    isLoading,
    error,
    createReport,
    deleteReport,
    refreshReports: fetchReports
  };
};
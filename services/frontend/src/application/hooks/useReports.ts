import { useState, useEffect, useCallback } from 'react';
import { type Report } from '../../domain/models/Report';
import { ReportService } from '../services/ReportService';

export const useReports = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchReports = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await ReportService.getAll();
      setReports(data);
    } catch (err) {
      setError('Failed to fetch reports');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchReports();
  }, [fetchReports]);

  const createReport = async (title: string, clientId: string, transcript: string) => {
    setIsLoading(true);
    try {
      const newReport = await ReportService.create({ title, clientId, transcript });
      setReports((prev) => [...prev, newReport]);
      return newReport;
    } catch (err) {
      setError('Failed to create report');
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

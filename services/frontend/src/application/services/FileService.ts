import { API_BASE_URL } from '../config/env';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

export const FileService = {
  getPreSignedUrl: async (fileName: string, contentType: string): Promise<{ uploadUrl: string; fileKey: string }> => {
    const response = await fetch(`${API_BASE_URL}/files/presigned-url`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // <-- Adicionado apenas aqui (vai para o backend)
      body: JSON.stringify({ fileName, contentType }),
    });
    if (!response.ok) throw new Error('Failed to get pre-signed URL');
    const result: ApiResponse<{ uploadUrl: string; fileKey: string }> = await response.json();
    return result.dado;
  },

  getFileUrlFromUploadUrl: (uploadUrl: string): string => {
    try {
      const url = new URL(uploadUrl);
      url.search = '';
      url.hash = '';
      return url.toString();
    } catch {
      return uploadUrl.split('?')[0] ?? uploadUrl;
    }
  },

  uploadToS3: async (uploadUrl: string, file: File): Promise<void> => {
    const response = await fetch(uploadUrl, {
      method: 'PUT',
      headers: {
        'Content-Type': file.type,
      },
      // ATENÇÃO: Nunca envie credentials: 'include' para o S3/AWS!
      body: file,
    });
    if (!response.ok) throw new Error('Failed to upload file to S3');
  }
};

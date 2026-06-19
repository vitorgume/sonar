const INVALID_DATE_LABEL = 'Data indisponivel';

const parseDateValue = (value: unknown): Date | null => {
  if (value == null) {
    return null;
  }

  if (value instanceof Date) {
    return Number.isNaN(value.getTime()) ? null : value;
  }

  if (typeof value === 'number') {
    if (value <= 0) {
      return null;
    }

    const normalizedValue = value < 1_000_000_000_000 ? value * 1000 : value;
    const parsedDate = new Date(normalizedValue);
    return Number.isNaN(parsedDate.getTime()) ? null : parsedDate;
  }

  if (typeof value === 'string') {
    const trimmedValue = value.trim();

    if (!trimmedValue) {
      return null;
    }

    if (/^\d+$/.test(trimmedValue)) {
      return parseDateValue(Number(trimmedValue));
    }

    const parsedDate = new Date(trimmedValue);
    return Number.isNaN(parsedDate.getTime()) ? null : parsedDate;
  }

  return null;
};

export const formatDate = (
  value: unknown,
  options?: Intl.DateTimeFormatOptions
): string => {
  const parsedDate = parseDateValue(value);

  if (!parsedDate) {
    return INVALID_DATE_LABEL;
  }

  return parsedDate.toLocaleDateString('pt-BR', options);
};

export const formatDateTime = (
  value: unknown,
  options?: Intl.DateTimeFormatOptions
): string => {
  const parsedDate = parseDateValue(value);

  if (!parsedDate) {
    return INVALID_DATE_LABEL;
  }

  return parsedDate.toLocaleString('pt-BR', options);
};

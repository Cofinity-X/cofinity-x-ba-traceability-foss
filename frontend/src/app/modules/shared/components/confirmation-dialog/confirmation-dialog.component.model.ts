export interface ConfirmationDialogComponentData {
  title: string;
  message: string;
  confirmButtonLabel: string;
  cancelButtonLabel?: string;

  confirmAction: () => void;
}
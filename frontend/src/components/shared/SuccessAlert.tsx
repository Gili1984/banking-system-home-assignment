import React, { useEffect, useState } from "react";

interface SuccessAlertProps {
  message: string;
  duration?: number; 
  onClose?: () => void;
}

export const SuccessAlert: React.FC<SuccessAlertProps> = ({
  message,
  duration = 5000,
  onClose,
}) => {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
      if (onClose) onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [duration, onClose]);

  if (!visible) return null;

  return (
    <div className="alert alert-success" role="alert">
      {message}
    </div>
  );
};

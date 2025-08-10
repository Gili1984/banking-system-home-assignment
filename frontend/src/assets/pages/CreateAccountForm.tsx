import React, { useState } from "react";
import { useCreateAccount } from "../hooks/useCreateAccount";
import { SuccessAlert } from "../../components/shared/SuccessAlert";
import { useNavigate } from "react-router-dom";


export const CreateAccountForm: React.FC = () => {
  const [showAlert, setShowAlert] = useState(false);
  const [accountNumber, setAccountNumber] = useState("");
  const navigate = useNavigate();
  const { mutate, isPending } = useCreateAccount((newAccountNumber) => {
    setAccountNumber(newAccountNumber);
    setShowAlert(true);
  });

  const [formData, setFormData] = useState({
    customerId: '',
    accountType: 'SAVINGS',
    currency: 'USD',
  });

  const handleChange = (e: {target: { name: any; value: any; }; preventDefault: () => void;}) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

    const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutate(formData); 
  };



  return (
    <div className="container mt-4" style={{ maxWidth: "400px" }}>
              {showAlert ? (
        <SuccessAlert
          message={`החשבון נוצר בהצלחה! מספר החשבון הוא ${accountNumber}`}
          duration={5000}
          onClose={() => navigate("/")}
        />
      ) : (
      <>
      <h4 className="mb-4 text-primary">Create Bank Account</h4>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="customerId" className="form-label">
            Customer ID
          </label>
          <input
            type="text"
            className="form-control"
            id="customerId"
            name="customerId"
            value={formData.customerId}
            onChange={handleChange}
            placeholder="Enter Customer ID"
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="accountType" className="form-label">
            סוג חשבון:
          </label>
          <select
            className="form-select"
            id="accountType"
            name="accountType"
            value={formData.accountType}
            onChange={handleChange}
          >
            <option value="CHECKING">עו"ש (עובר ושב)</option>
            <option value="SAVINGS">חיסכון</option>
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="currency" className="form-label">
            סוג מטבע:
          </label>
          <select
            className="form-select"
            id="currency"
            name="currency"
            value={formData.currency}
            onChange={handleChange}
          >
            <option value="USD">USD - ארה"ב</option>
            <option value="EUR">EUR - אירופה</option>
            <option value="ILS">ILS - ישראלי</option>
          </select>
        </div>
       <button
          type="submit"
          className="btn btn-primary w-100"
          disabled={isPending}
        >
          {isPending ? "שולח..." : "צור חשבון"}
        </button>
      </form>
      </>
    )}
    </div>
  );
}

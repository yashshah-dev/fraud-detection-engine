# Credit Card Fraud Detection ML API

This project demonstrates a machine learning pipeline for real-time credit card fraud detection. It includes model training and a FastAPI-based REST API for risk scoring.

## Features
- Trains a RandomForest model using the public credit card fraud dataset (V1–V28, Amount)
- FastAPI endpoint for real-time risk score prediction
- Random feature generation for demo/testing
- Version-pinned requirements and .gitignore
- Simple run script for local development

## Project Structure
```
card-ml-model/
├── app/
│   └── main.py           # FastAPI app
├── model/
│   └── train_model.py    # Model training script
├── requirements.txt      # Python dependencies
├── .gitignore            # Git ignore rules
├── run_project.py        # Script to train and run API
```

## Getting Started

### 1. Install dependencies
```sh
pip install -r requirements.txt
```

### 2. (Optional) Download the dataset
Place `creditcard.csv` from [Kaggle](https://www.kaggle.com/mlg-ulb/creditcardfraud) in the `model/` folder for real-data training.

### 3. Run the project
```sh
python run_project.py
```
- The API will be available at http://127.0.0.1:8000
- Swagger docs: http://127.0.0.1:8000/docs

### 4. Example API Request
POST `/predict`
```json
{
  "amount": 100.0
}
```

## Notes
- If the real dataset is not present, the model will use simulated data.
- For demo, V1–V28 features are randomly generated for each prediction.

## License
MIT

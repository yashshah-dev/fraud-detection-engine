# FastAPI app for fraud detection
from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np

app = FastAPI()


model = None
feature_names = None
random_feature_samples = None

# Example: Accept real-world-like attributes
class Transaction(BaseModel):
    amount: float
    V1: float = None
    V2: float = None
    V3: float = None
    V4: float = None
    V5: float = None
    V6: float = None
    V7: float = None
    V8: float = None
    V9: float = None
    V10: float = None
    V11: float = None
    V12: float = None
    V13: float = None
    V14: float = None
    V15: float = None
    V16: float = None
    V17: float = None
    V18: float = None
    V19: float = None
    V20: float = None
    V21: float = None
    V22: float = None
    V23: float = None
    V24: float = None
    V25: float = None
    V26: float = None
    V27: float = None
    V28: float = None
    card_type: str = 'credit'  # demo only
    merchant_category: str = 'grocery'  # demo only
    country: str = 'US'  # demo only
    # Add more as needed for demo

def map_to_model_features(transaction: Transaction):
    # Use actual amount and user-provided V1-V28 if present, else random
    features = []
    for name in feature_names:
        if name.lower() == 'amount':
            features.append(transaction.amount)
    for val in range(0,28):
        features.append(np.random.normal(0, 1))  # Simulate V1-V28 if not provided
    return np.array([features])


import pandas as pd
import os
import random

@app.on_event("startup")
def load_model():
    global model, feature_names, random_feature_samples
    obj = joblib.load('model/model.joblib')
    if isinstance(obj, dict):
        model = obj['model']
        feature_names = obj['features']
    else:
        model = obj
        feature_names = ['amount', 'feature1', 'feature2']  # fallback
    # Try to load random samples for anonymized features
    csv_path = 'model/creditcard.csv'
    if os.path.exists(csv_path):
        data = pd.read_csv(csv_path)
        feature_cols = [col for col in data.columns if col not in ['Time', 'Class', 'Amount']]
        random_feature_samples = data[feature_cols]
    else:
        random_feature_samples = None

@app.post("/predict")
def predict(transaction: Transaction):
    X = map_to_model_features(transaction)
    #print(X)
    score = model.predict_proba(X)[0, 1]
    return {"risk_score": float(score)}

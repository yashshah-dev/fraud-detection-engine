# This script will be used to train and save the fraud detection model

import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import roc_auc_score
import joblib
import numpy as np

# Try to load real dataset, else fallback to simulation
import os
csv_path = 'model/transactions.csv'
if os.path.exists(csv_path):
    print('Loading real dataset...')
    data = pd.read_csv(csv_path)
    # Use all features except 'Time' and 'Class'
    feature_cols = [col for col in data.columns if col not in ['Time', 'Class']]
    X = data[feature_cols]
    y = data['Class']
else:
    print('Real dataset not found, using simulated data.')
    np.random.seed(42)
    num_samples = 1000
    X = pd.DataFrame({
        'Amount': np.random.uniform(1, 500, num_samples),
        'V1': np.random.randn(num_samples),
        'V2': np.random.randn(num_samples),
        'V3': np.random.randn(num_samples),
        'V4': np.random.randn(num_samples),
        'V5': np.random.randn(num_samples),
        'V6': np.random.randn(num_samples),
        'V7': np.random.randn(num_samples),
        'V8': np.random.randn(num_samples),
        'V9': np.random.randn(num_samples),
        'V10': np.random.randn(num_samples),
        'V11': np.random.randn(num_samples),
        'V12': np.random.randn(num_samples),
        'V13': np.random.randn(num_samples),
        'V14': np.random.randn(num_samples),
        'V15': np.random.randn(num_samples),
        'V16': np.random.randn(num_samples),
        'V17': np.random.randn(num_samples),
        'V18': np.random.randn(num_samples),
        'V19': np.random.randn(num_samples),
        'V20': np.random.randn(num_samples),
        'V21': np.random.randn(num_samples),
        'V22': np.random.randn(num_samples),
        'V23': np.random.randn(num_samples),
        'V24': np.random.randn(num_samples),
        'V25': np.random.randn(num_samples),
        'V26': np.random.randn(num_samples),
        'V27': np.random.randn(num_samples),
        'V28': np.random.randn(num_samples),
    })
    y = np.random.binomial(1, 0.05, num_samples)  # 5% fraud

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

y_pred = model.predict_proba(X_test)[:, 1]
auc = roc_auc_score(y_test, y_pred)
print(f'AUC: {auc:.3f}')

# Save model and feature columns
joblib.dump({'model': model, 'features': list(X.columns)}, 'model/model.joblib')
print('Model and feature list saved to model/model.joblib')

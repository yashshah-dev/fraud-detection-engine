# This script downloads the public Kaggle credit card fraud dataset
import pandas as pd
import requests
import zipfile
import io

# Download dataset from public source
url = 'https://storage.googleapis.com/download.tensorflow.org/data/creditcard.csv'
output_path = 'model/transactions.csv'

print('Downloading dataset...')
df = pd.read_csv(url)
df.to_csv(output_path, index=False)
print(f'Dataset saved to {output_path}')

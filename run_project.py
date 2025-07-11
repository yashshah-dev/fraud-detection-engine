import subprocess
import sys

# Step 1: Train the model
#print('Training the model...')
#subprocess.run([sys.executable, 'model/train_model.py'], check=True)

# Step 2: Start the FastAPI server
print('Starting FastAPI server at http://127.0.0.1:8000 ...')
uvicorn_cmd = [sys.executable.replace('python.exe', 'uvicorn.exe'), 'app.main:app', '--reload']
subprocess.run(uvicorn_cmd)

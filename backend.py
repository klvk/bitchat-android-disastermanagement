from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/verify', methods=['POST'])
def verify_disaster():
    """
    Mock endpoint to verify if an uploaded image and text represent a disaster.
    In a real scenario, this would use ML models.
    """
    description = request.form.get('description')
    image = request.files.get('image')

    print(f"Received verification request. Description: {description}")
    if image:
        print(f"Received image: {image.filename}")

    # Simulate processing logic
    # For demo purposes, we always return True if "disaster" is in description,
    # or randomly return True.
    # Here let's just say it's always valid for testing purposes.
    is_valid_disaster = True

    return jsonify({
        "verified": is_valid_disaster,
        "message": "Disaster verification successful" if is_valid_disaster else "Not a disaster"
    })

if __name__ == '__main__':
    # Run on 0.0.0.0 so it's accessible from Android Emulator via 10.0.2.2
    app.run(host='0.0.0.0', port=5000)

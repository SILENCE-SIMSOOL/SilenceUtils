import json

input_file = "input.json"
output_file = "output.json"

def transform_json(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    transformed_data = []

    for room in data:
        new_room = {
            "name": room.get("name"),
            "type": room.get("type"),
            "secret": room.get("secrets", 0),
            "id": room.get("cores", []),
            "waypoints": []
        }
        transformed_data.append(new_room)
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(transformed_data, f, indent=2, ensure_ascii=False)

transform_json(input_file, output_file)
print(f"Transformed JSON saved to {output_file}")

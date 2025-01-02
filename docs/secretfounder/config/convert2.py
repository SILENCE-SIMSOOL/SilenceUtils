import json

input_file = "input.json"
output_file = "output.json"

with open(input_file, 'r') as f:
    roomdata = json.load(f)
rooms = []

for room in roomdata:
    new_room = {
        "name": room.get("name"),
        "secret": room.get("secrets", 0),
        "id": room.get("cores", []),
        "type": room.get("type").upper() if room.get("type") else "NORMAL",
        "waypoints": []
    }
    if "secret_coords" in room:
        for category, coords in room["secret_coords"].items():
            for coord in coords:
                new_room["waypoints"].append({
                    "x": coord[0],
                    "y": coord[1],
                    "z": coord[2],
                    "category": category
                })
    rooms.append(new_room)

with open(output_file, 'w') as f:
    json.dump(rooms, f, indent=2)

print(f"Transformed JSON saved to {output_file}")

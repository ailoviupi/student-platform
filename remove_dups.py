import re
import os

def remove_duplicates(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    seen = set()
    new_lines = []
    for line in lines:
        # Check if this is a getter/setter method
        stripped = line.strip()
        if stripped.startswith('public ') and ('get' in stripped or 'set' in stripped) and '{ return' in stripped:
            # Extract method signature
            match = re.match(r'publics+w+s+(w+([^)]*))', stripped)
            if match:
                sig = match.group(1)
                if sig in seen:
                    continue  # Skip duplicate
                seen.add(sig)
        new_lines.append(line)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

# Find all DTO files
for root, dirs, files in os.walk('student-platform/backend/src/main/java/com/snvec/student/modules'):
    for filename in files:
        if filename.endswith('.java') and 'dto' in root:
            filepath = os.path.join(root, filename)
            remove_duplicates(filepath)
            print(f"Checked: {filename}")

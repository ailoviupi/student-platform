import re
import os

def fix_dto(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Find all private fields (including those with default values)
    field_pattern = r'private\s+(\w+(?:<[^>]+>)?)\s+(\w+)(?:\s*=\s*[^;]+)?\s*;'
    fields = re.findall(field_pattern, content)
    
    if not fields:
        return False
    
    # Check which getters/setters are missing
    missing = []
    for type_name, field_name in fields:
        if field_name == 'serialVersionUID':
            continue
        cap_name = field_name[0].upper() + field_name[1:]
        getter = f'get{cap_name}()'
        setter = f'set{cap_name}('
        if getter not in content or setter not in content:
            missing.append((type_name, field_name))
    
    if not missing:
        return False
    
    # Generate missing getters/setters
    getters_setters = []
    for type_name, field_name in missing:
        cap_name = field_name[0].upper() + field_name[1:]
        getter = f"    public {type_name} get{cap_name}() {{ return {field_name}; }}"
        setter = f"    public void set{cap_name}({type_name} {field_name}) {{ this.{field_name} = {field_name}; }}"
        getters_setters.append(getter)
        getters_setters.append(setter)
    
    # Insert before last closing brace
    last_brace = content.rfind('}')
    new_content = content[:last_brace] + '\n' + '\n'.join(getters_setters) + '\n' + content[last_brace:]
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    return True

# Find all DTO files
for root, dirs, files in os.walk('student-platform/backend/src/main/java/com/snvec/student/modules'):
    for filename in files:
        if filename.endswith('.java') and 'dto' in root:
            filepath = os.path.join(root, filename)
            if fix_dto(filepath):
                print(f"Fixed: {os.path.basename(filepath)}")

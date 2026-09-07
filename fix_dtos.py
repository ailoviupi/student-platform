import re
import os

def add_getters_setters(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Find all private fields (excluding static and serialVersionUID)
    field_pattern = r'private\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*;'
    fields = re.findall(field_pattern, content)
    
    if not fields:
        return False
    
    # Generate getters/setters
    getters_setters = []
    for type_name, field_name in fields:
        if field_name == 'serialVersionUID':
            continue
        # Capitalize first letter
        cap_name = field_name[0].upper() + field_name[1:]
        
        # Generate getter
        getter = f"    public {type_name} get{cap_name}() {{ return {field_name}; }}"
        # Generate setter
        setter = f"    public void set{cap_name}({type_name} {field_name}) {{ this.{field_name} = {field_name}; }}"
        
        getters_setters.append(getter)
        getters_setters.append(setter)
    
    if not getters_setters:
        return False
    
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
            if add_getters_setters(filepath):
                print(f"Fixed: {filepath}")

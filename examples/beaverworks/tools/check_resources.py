"""Check resource consistency without downloading Minecraft or Forge."""
from pathlib import Path
import json
import struct
p = Path(__file__).resolve().parents[1] / 'src/main/resources'
files = list(p.rglob('*.json')) + list(p.rglob('*.mcmeta'))
for f in files:
    json.loads(f.read_text(encoding='utf-8'))
models = p / 'assets/beaverworks/models/item'
ids = {f.stem for f in models.glob('*.json')}
assert len(ids) == 10
for f in models.glob('*.json'):
    tex = json.loads(f.read_text())['textures']['layer0'].split(':')[1]
    assert (p / f'assets/beaverworks/textures/{tex}.png').exists(), tex
for f in (p / 'data/beaverworks/recipes').glob('*.json'):
    data = json.loads(f.read_text())
    assert data['result']['item'].split(':')[1] in ids
    if 'key' in data:
        assert set(''.join(data['pattern'])) - {' '} == set(data['key'])
for lang in ('ru_ru', 'en_us'):
    data = json.loads((p / f'assets/beaverworks/lang/{lang}.json').read_text(encoding='utf-8'))
    assert all(f'item.beaverworks.{item}' in data for item in ids)
for f in p.rglob('*.png'):
    data = f.read_bytes()
    assert data[:8] == b'\x89PNG\r\n\x1a\n'
    assert struct.unpack('>II', data[16:24]) == ((64, 32) if 'armor' in str(f) else (16, 16)), f
print('PASS: JSON, 10 models/recipes, translations and texture dimensions')

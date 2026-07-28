#!/usr/bin/env bash
# =============================================================================
# publish-wiki.sh
# Script per publicar els fitxers de la carpeta wiki/ al GitHub Wiki
#
# Ús:
#   chmod +x publish-wiki.sh
#   ./publish-wiki.sh https://github.com/USUARI/DAMPJ6-AndroidMultimedia.git
#
# Prerequisits:
#   - git instal·lat
#   - Accés de push al repositori GitHub
#   - La Wiki ja ha d'estar habilitada al repositori (Settings → Features → Wikis)
# =============================================================================

set -e

REPO_URL="${1}"

if [ -z "$REPO_URL" ]; then
  echo "❌ Cal especificar la URL del repositori."
  echo "   Ús: ./publish-wiki.sh https://github.com/USUARI/REPO.git"
  exit 1
fi

# La URL del wiki afegeix .wiki al nom del repo
WIKI_URL="${REPO_URL%.git}.wiki.git"

echo "📖 Publicant wiki a: $WIKI_URL"
echo ""

# Directori temporal
TMPDIR=$(mktemp -d)
WIKI_DIR="$(dirname "$0")/wiki"

# Clonar el repositori wiki (ha d'existir - crea una pàgina manual primer)
echo "📥 Clonant el repositori wiki..."
git clone "$WIKI_URL" "$TMPDIR" 2>/dev/null || {
  echo "⚠️  No s'ha pogut clonar. Inicialitzant repositori wiki buit..."
  git init "$TMPDIR"
  cd "$TMPDIR"
  git remote add origin "$WIKI_URL"
  cd - > /dev/null
}

# Copiar tots els fitxers .md de la carpeta wiki/
echo "📋 Copiant fitxers de la wiki..."
cp "$WIKI_DIR"/*.md "$TMPDIR/"

# Commit i push
cd "$TMPDIR"
git add -A
git commit -m "docs: actualitzar wiki del projecte DAMPJ6-AndroidMultimedia

- Home: índex complet amb tots els mòduls
- M01-M21: documentació detallada de cada exercici
- Conceptes, exemples de codi Kotlin, diagrames i taules comparatives
- Actualitzat per a Kotlin 2.1.0 i API 36 (Android 16)"

echo "📤 Pujant la wiki a GitHub..."
git push -u origin master 2>/dev/null || git push -u origin main

echo ""
echo "✅ Wiki publicada correctament!"
echo "   Accedeix a: ${REPO_URL%.git}/wiki"

# Netejar directori temporal
cd - > /dev/null
rm -rf "$TMPDIR"


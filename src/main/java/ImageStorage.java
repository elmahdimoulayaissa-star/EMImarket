import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class ImageStorage {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private ImageStorage() {}

    public static boolean isAcceptedImageReference(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        return hasAllowedExtension(value.trim());
    }

    public static String normalizeForStorage(String value) throws IOException {
        if (!isAcceptedImageReference(value)) {
            throw new IOException("Image invalide. Utilisez une URL (https://...) ou un fichier local .jpg/.png/.gif/.webp");
        }

        String trimmed = value.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }

        if (trimmed.startsWith("file:/")) {
            try {
                return importLocalImage(new File(new URI(trimmed)));
            } catch (URISyntaxException e) {
                throw new IOException("Chemin d'image invalide.", e);
            }
        }

        File file = new File(trimmed);
        if (file.exists()) {
            return importLocalImage(file);
        }

        return trimmed;
    }

    public static String importLocalImage(File source) throws IOException {
        if (source == null || !source.exists()) {
            throw new IOException("Image introuvable.");
        }
        if (!hasAllowedExtension(source.getName())) {
            throw new IOException("Format d'image non supporté.");
        }

        Path imagesDir = BD.getImagesDir();
        Files.createDirectories(imagesDir);
        String extension = extractExtension(source.getName());
        Path target = imagesDir.resolve(UUID.randomUUID() + extension);
        Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public static String toLoadableUri(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        String trimmed = value.trim();

        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("file:/")) {
            return trimmed;
        }

        File file = new File(trimmed);
        if (file.exists()) {
            return file.toURI().toString();
        }

        return trimmed;
    }

    private static boolean hasAllowedExtension(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        int queryIndex = normalized.indexOf('?');
        if (queryIndex >= 0) normalized = normalized.substring(0, queryIndex);
        for (String ext : ALLOWED_EXTENSIONS) {
            if (normalized.endsWith(ext)) return true;
        }
        return false;
    }

    private static String extractExtension(String name) {
        int idx = name.lastIndexOf('.');
        return idx >= 0 ? name.substring(idx).toLowerCase(Locale.ROOT) : ".img";
    }
}

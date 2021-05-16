package tr.edu.yildiz.mustafabugrayilmaz;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Tools {
    public static Uri saveImageToInternal(Context context, Uri uri, String fileName) {
        try {
            Uri iconUri;

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            File profileFile = new File(context.getFilesDir(), fileName);

            FileOutputStream outputStream = new FileOutputStream(profileFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            File temp = new File(context.getFilesDir(), fileName);

            iconUri = Uri.fromFile(temp);

            outputStream.close();

            return iconUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Uri saveFileToExternal(Context context, Uri uri, String fileName){
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));

        File file=new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName+"."+type);

        try (InputStream in = context.getContentResolver().openInputStream(uri)) {
            try (OutputStream out = new FileOutputStream(file)) {
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                return Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileName(Context context, String uriString) {
        Uri uri=Uri.parse(uriString);

        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}

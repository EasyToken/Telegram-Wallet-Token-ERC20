package info.bcdev.telegramwallet.qr;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.zxing.BarcodeFormat.QR_CODE;

public class QRCode{

    public Path qrGen(String walletaddress, String imgDir){
        Path file = Paths.get(imgDir+walletaddress+".png");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(walletaddress, QR_CODE, 200,200);
            try {
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return file;
    }
}

package com.dodou.liwh.qrcode.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Map;

/**
 * @author: Lwh
 * @ClassName: QRCodeWriter
 * @Description:
 * @version: 1.0.0
 * @date: 2019-03-09 6:59 PM
 */
public class QRCodeWriter2 {
    private static final int QUIET_ZONE_SIZE = 4;

    public QRCodeWriter2() {
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return this.encode(contents, format, width, height, (Map)null);
    }

    public static BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        } else if (format != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        } else if (width >= 0 && height >= 0) {
            ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
            int quietZone = 4;
            if (hints != null) {
                if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                    errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
                }

                if (hints.containsKey(EncodeHintType.MARGIN)) {
                    quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
                }
            }

            return renderResult(Encoder.encode(contents, errorCorrectionLevel, hints), width, height, quietZone);
        } else {
            throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
        }
    }

    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        // 依据用户的输入宽高，计算最后的输出宽高
        int outputWidth = Math.max(width, inputWidth);
        int outputHeight = Math.max(height, inputHeight);
        //计算缩放比例
        int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);
        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        int inputY = 0;
        // 嵌套循环，将ByteMatrix的内容计算padding后转换成BitMatrix
        for (int outputY = 0; inputY < inputHeight; outputY += multiple) {
            int inputX = 0;
            for (int outputX = 0; inputX < inputWidth; outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
                inputX++;
            }
            inputY++;
        }
        return output;
    }
}

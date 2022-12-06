import java.awt.Color;

public class contrast extends filter {

    public contrast(String name) {
        super(name);
    }


    public void apply(OFImage image) {

        int height = image.getHeight();
        int width = image.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color pix = image.getPixel(x, y);

                int red = pix.getRed();
                int green = pix.getGreen();
                int blue = pix.getBlue();

                //changes from red-green0blue to hue-saturation-brightness
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);

                float hue = hsb[0];
                float saturation = hsb[2];
                float brightness = hsb[1];

                //manipulates the colours
                //changes the saturation
                int rgb = Color.HSBtoRGB(hue, saturation, brightness);

                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;

                image.setPixel(x, y, new Color(red, green, blue));
            }
        }
    }
}


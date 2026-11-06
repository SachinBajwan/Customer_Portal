package terms.erp.mainapp.view.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CaptchaImageServlet", urlPatterns = { "/CaptchaImageServlet" })
public class CaptchaImageServlet extends HttpServlet {
    private static final int WIDTH = 180;
        private static final int HEIGHT = 45;

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setDateHeader("Expires", 0);

            // Generate a random 6-character alphanumeric CAPTCHA code
            String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
            StringBuilder captchaCode = new StringBuilder();
            Random rnd = new Random();
            for (int i = 0; i < 6; i++) {
                captchaCode.append(chars.charAt(rnd.nextInt(chars.length())));
            }

            // Store code in session for validation
            HttpSession session = request.getSession();
            session.setAttribute("SESSION_CAPTCHA", captchaCode.toString());

            // Create image buffer
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();

            // Background gradient / fill
            g2d.setColor(new Color(240, 244, 248));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            // Draw random noise lines for security
            g2d.setColor(new Color(200, 210, 220));
            for (int i = 0; i < 8; i++) {
                int x1 = rnd.nextInt(WIDTH);
                int y1 = rnd.nextInt(HEIGHT);
                int x2 = rnd.nextInt(WIDTH);
                int y2 = rnd.nextInt(HEIGHT);
                g2d.drawLine(x1, y1, x2, y2);
            }

            // Draw CAPTCHA text characters with slight rotations & fonts
            g2d.setFont(new Font("Arial", Font.BOLD, 26));
            int x = 15;
            for (int i = 0; i < captchaCode.length(); i++) {
                g2d.setColor(new Color(20 + rnd.nextInt(80), 40 + rnd.nextInt(80), 100 + rnd.nextInt(100)));
                AffineTransform origTransform = g2d.getTransform();
                
                double angle = (rnd.nextDouble() - 0.5) * 0.4; // slight rotation
                g2d.rotate(angle, x, 30);
                g2d.drawString(String.valueOf(captchaCode.charAt(i)), x, 32);
                
                g2d.setTransform(origTransform);
                x += 25;
            }

            g2d.dispose();
            ImageIO.write(img, "png", response.getOutputStream());
        }
}

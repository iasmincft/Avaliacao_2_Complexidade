package br.edu.ifba.encriptador.aleatoriedade;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.edu.ifba.encriptador.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeAleatoriedadeReal
        extends SecureRandom {

        private FFmpegFrameGrabber grabber;

        public GeradorDeAleatoriedadeReal(
                String caminhoVideo
        ) throws FalhaGeracaoDeChaves {

                try {

                Loader.load(
                        org.bytedeco.opencv.global.opencv_core.class
                );

                grabber =
                        new FFmpegFrameGrabber(
                                caminhoVideo
                        );

                grabber.start();

                } catch (Exception e) {

                throw new FalhaGeracaoDeChaves(
                        e.getMessage()
                );
                }
        }

        @Override
        public int nextInt() {

                try {

                Frame frame =
                        grabber.grabImage();

                if (frame == null) {
                        return super.nextInt();
                }

                Java2DFrameConverter converter =
                        new Java2DFrameConverter();

                BufferedImage image =
                        converter.convert(frame);

                ByteArrayOutputStream stream =
                        new ByteArrayOutputStream();

                ImageIO.write(
                        image,
                        "jpg",
                        stream
                );

                byte[] bytes =
                        stream.toByteArray();

                int valor = 0;

                for (int i = 0;
                        i < 4 && i < bytes.length;
                        i++) {

                        valor <<= 8;
                        valor |= bytes[i] & 0xff;
                }

                return valor;

                } catch (Exception e) {

                return super.nextInt();
                }
        }

        public void finalizar()
                throws FalhaGeracaoDeChaves {

                try {

                grabber.stop();
                grabber.release();

                } catch (Exception e) {

                throw new FalhaGeracaoDeChaves(
                        e.getMessage()
                );
                }
        }
}
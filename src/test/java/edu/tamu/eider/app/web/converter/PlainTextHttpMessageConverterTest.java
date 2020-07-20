package edu.tamu.eider.app.web.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

@SpringBootTest
public class PlainTextHttpMessageConverterTest {

  @Test
  void testPlainTextHttpMessageConverter() throws HttpMessageNotReadableException, IOException {
      String message = "Hello, Tests!";

      PlainTextHttpMessageConverter converter = new PlainTextHttpMessageConverter();

      HttpInputMessage inputMessage = new MockHttpInputMessage(message.getBytes());
      String actual = converter.readInternal(String.class, inputMessage);
      assertEquals(message, actual);

      HttpOutputMessage outputMessage = new MockHttpOutputMessage();
      converter.writeInternal(message, outputMessage);

      ByteArrayOutputStream baos =(ByteArrayOutputStream) outputMessage.getBody();
      assertEquals(message, new String(baos.toByteArray()));
    }
  
}
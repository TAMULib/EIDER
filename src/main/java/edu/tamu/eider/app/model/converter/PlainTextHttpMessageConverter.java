package edu.tamu.eider.app.model.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class PlainTextHttpMessageConverter extends AbstractHttpMessageConverter<String> {

  public PlainTextHttpMessageConverter() {
    super(MediaType.TEXT_PLAIN);
 }

  @Override
  protected boolean supports(Class<?> clazz) {
    return String.class == clazz;
  }

  @Override
  protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    try(InputStream in = inputMessage.getBody()) {
      return StreamUtils.copyToString(in, Charset.defaultCharset());
    }
  }

  @Override
  protected void writeInternal(String value, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
      try (PrintWriter writer = new PrintWriter(outputMessage.getBody())) {
          writer.println(value);
      } 
  }
  
}

package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyDto;
import dto.ErrorDto;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;

import static util.validation.FormFieldValidator.validateCode;
import static util.validation.PathValidator.validatePathWithCurrencyCode;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ObjectMapper objectMapper;

    public CurrencyServlet() {
        this.currencyService = CurrencyService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String path = req.getPathInfo();
                validatePathWithCurrencyCode(path);

                String code = path.replaceAll("/", "");
                validateCode(code);

                CurrencyDto currencyDto = currencyService.findByCode(code);
                objectMapper.writeValue(writer, currencyDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}

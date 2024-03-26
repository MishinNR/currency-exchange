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
import util.validation.FormFieldValidator;
import util.validation.PathValidator;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final FormFieldValidator formFieldValidator;
    private final PathValidator pathValidator;
    private final ObjectMapper objectMapper;

    public CurrencyServlet() {
        this.currencyService = CurrencyService.getInstance();
        this.formFieldValidator = FormFieldValidator.getInstance();
        this.pathValidator = PathValidator.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String path = req.getPathInfo();
                pathValidator.validatePathWithCurrencyCode(path);

                String code = path.replaceAll("/", "");
                formFieldValidator.validateCode(code);

                CurrencyDto currencyDto = currencyService.findByCode(code);
                objectMapper.writeValue(writer, currencyDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}

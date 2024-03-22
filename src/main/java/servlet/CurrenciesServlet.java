package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyDto;
import dto.ErrorDto;
import entity.Currency;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import util.validation.FormFieldValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final FormFieldValidator formFieldValidator;
    private final ObjectMapper objectMapper;

    public CurrenciesServlet() {
        this.currencyService = CurrencyService.getInstance();
        this.formFieldValidator = new FormFieldValidator();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                List<CurrencyDto> currencies = currencyService.findAll();
                objectMapper.writeValue(writer, currencies);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String name = req.getParameter("name");
                String code = req.getParameter("code");
                String sign = req.getParameter("sign");

                formFieldValidator.validateName(name);
                formFieldValidator.validateCode(code);
                formFieldValidator.validateSign(sign);

                CurrencyDto currencyDto = currencyService.save(new Currency(code, name, sign));
                objectMapper.writeValue(writer, currencyDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}

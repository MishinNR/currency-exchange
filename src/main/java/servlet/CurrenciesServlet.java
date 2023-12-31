package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyDTO;
import dto.ErrorDTO;
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
        this.formFieldValidator = FormFieldValidator.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                List<CurrencyDTO> currencies = currencyService.findAll();
                objectMapper.writeValue(writer, currencies);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
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

                CurrencyDTO currencyDTO = currencyService.save(new Currency(code, name, sign));
                objectMapper.writeValue(writer, currencyDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }
}

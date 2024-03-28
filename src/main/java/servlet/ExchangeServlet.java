package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDto;
import dto.ExchangeDto;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static util.validation.FormFieldValidator.validateAmount;
import static util.validation.FormFieldValidator.validateCode;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService;
    private final ObjectMapper objectMapper;

    public ExchangeServlet() {
        this.exchangeService = ExchangeService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String fromCurrency = req.getParameter("from");
                String toCurrency = req.getParameter("to");
                String amount = req.getParameter("amount");

                validateCode(fromCurrency);
                validateCode(toCurrency);
                validateAmount(amount);

                ExchangeDto exchangeDto = exchangeService.exchange(
                        fromCurrency.toUpperCase(),
                        toCurrency.toUpperCase(),
                        new BigDecimal(amount)
                );
                objectMapper.writeValue(writer, exchangeDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}

package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDTO;
import dto.ExchangeRateDTO;
import entity.ExchangeRate;
import exception.ApplicationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.ExchangeRateConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static util.ValidationUtil.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper;
    private final ExchangeRateConverter exchangeRateConverter;

    public ExchangeRateServlet() {
        this.exchangeRateService = ExchangeRateService.getInstance();
        this.objectMapper = new ObjectMapper();
        this.exchangeRateConverter = new ExchangeRateConverter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String path = req.getPathInfo();
                validateCurrencyCodeExistsInAddress(path);

                String pairCode = path.replaceAll("/", "").toUpperCase();
                validateParameterPairCode(pairCode);

                String baseCode = pairCode.substring(0, 3);
                String targetCode = pairCode.substring(3, 6);

                ExchangeRateDTO exchangeRateDTO = exchangeRateService.findByBaseAndTargetCodes(baseCode, targetCode);
                objectMapper.writeValue(writer, exchangeRateDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }
        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String rate = req.getParameter("rate");
                validateRequestParameterIsPresent(rate);
                validateParameterRate(rate);

                String path = req.getPathInfo();
                validateCurrencyCodeExistsInAddress(path);

                String pairCode = path.replaceAll("/", "").toUpperCase();
                validateParameterPairCode(pairCode);

                String baseCode = pairCode.substring(0, 3);
                String targetCode = pairCode.substring(3, 6);

                ExchangeRate exchangeRate = exchangeRateConverter.convertToEntity(exchangeRateService.findByBaseAndTargetCodes(baseCode, targetCode));
                exchangeRate.setRate(new BigDecimal(rate));
                ExchangeRateDTO updatedExchangeRateDTO = exchangeRateService.update(exchangeRate);
                objectMapper.writeValue(writer, updatedExchangeRateDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }
}

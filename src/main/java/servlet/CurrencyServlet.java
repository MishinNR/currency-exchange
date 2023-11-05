package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyDTO;
import dto.ErrorDTO;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;

import static util.ValidationUtil.validateCurrencyCodeExistsInAddress;
import static util.ValidationUtil.validateParameterCode;

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
                validateCurrencyCodeExistsInAddress(path);

                String code = path.replaceAll("/", "").toUpperCase();
                validateParameterCode(code);

                CurrencyDTO currencyDTO = currencyService.findByCode(code);
                objectMapper.writeValue(writer, currencyDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }
}

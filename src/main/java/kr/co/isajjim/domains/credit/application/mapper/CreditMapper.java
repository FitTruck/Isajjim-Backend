package kr.co.isajjim.domains.credit.application.mapper;

import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeConfirmResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeReadyResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.persistence.entity.CreditChargeOrder;
import kr.co.isajjim.domains.credit.persistence.entity.CreditTransaction;

public class CreditMapper {

    public static CreditChargeReadyResponse toChargeReadyResponse(CreditChargeOrder order, String clientKey, String successUrl, String failUrl) {
        return CreditChargeReadyResponse.builder()
                .orderId(order.getOrderId())
                .orderName(order.getOrderName())
                .amount(order.getAmount())
                .creditAmount(order.getCreditAmount())
                .clientKey(clientKey)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }

    public static CreditChargeConfirmResponse toChargeConfirmResponse(CreditChargeOrder order, Long balance) {
        return CreditChargeConfirmResponse.builder()
                .orderId(order.getOrderId())
                .chargedCredit(order.getCreditAmount())
                .balance(balance)
                .approvedAt(order.getApprovedAt())
                .build();
    }

    public static CreditBalanceResponse toBalanceResponse(Long balance) {
        return CreditBalanceResponse.builder()
                .balance(balance)
                .build();
    }

    public static CreditTransactionResponse fromCreditTransaction(CreditTransaction transaction) {
        return CreditTransactionResponse.builder()
                .transactionId(transaction.getId())
                .type(transaction.getType())
                .creditAmount(transaction.getCreditAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .referenceType(transaction.getReferenceType())
                .referenceId(transaction.getReferenceId())
                .createdDate(transaction.getCreatedDate())
                .build();
    }
}

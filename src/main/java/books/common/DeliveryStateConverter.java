package books.common;

public class DeliveryStateConverter {

    private DeliveryStateConverter() {

    }

    public static String deliveryStateToString(DeliveryState deliveryState) {
        switch (deliveryState) {
            case BEFORE:
                return "주문전";
            case PREPARING:
                return "준비중";
            case DELIVERING:
                return "배송중";
            case RETURN:
                return "환불";
            case CANCELED:
                return "주문취소";
            case CONFIRMED:
                return "구매확정";
            case DELIVERY_COMPLETED:
                return "배송완료";
            default:
                throw new IllegalArgumentException();
        }
    }

    public static DeliveryState stringToDeliveryState(String string) {
        switch (string) {
            case "주문전":
                return DeliveryState.BEFORE;
            case "준비중":
                return DeliveryState.PREPARING;
            case "배송중":
                return DeliveryState.DELIVERING;
            case "환불":
                return DeliveryState.RETURN;
            case "주문취소":
                return DeliveryState.CANCELED;
            case "구매확정":
                return DeliveryState.CONFIRMED;
            case "배송완료":
                return DeliveryState.DELIVERY_COMPLETED;
            default:
                throw new IllegalArgumentException();
        }
    }
}

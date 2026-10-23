document.addEventListener("DOMContentLoaded", () => {
    const panel = document.querySelector(".panel");
    const cancelOrderButton = document.getElementById("cancelOrderButton");
    const errorMessage = document.getElementById("errorMessage");

    if (!panel || !cancelOrderButton || !errorMessage) {
        return;
    }

    const orderId = panel.dataset.orderId;

    cancelOrderButton.addEventListener("click", async () => {
        if (!confirm("주문을 취소하시겠습니까?")) {
            return;
        }

        cancelOrderButton.disabled = true;
        errorMessage.hidden = true;
        errorMessage.textContent = "";

        try {
            const response = await fetch(
                `/api/orders/${encodeURIComponent(orderId)}`,
                {
                    method: "PATCH",
                    credentials: "same-origin"
                }
            );

            const result = await response.json();

            if (!response.ok || !result.isSuccess) {
                throw new Error(
                    result.message || "주문 취소에 실패했습니다."
                );
            }

            alert("주문이 취소되었습니다.");
            window.location.reload();
        } catch (error) {
            errorMessage.textContent =
                error.message || "주문 취소 중 오류가 발생했습니다.";

            errorMessage.hidden = false;
            cancelOrderButton.disabled = false;
        }
    });
});
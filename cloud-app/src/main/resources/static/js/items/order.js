document.addEventListener("DOMContentLoaded", () => {
    const itemIdElement = document.getElementById("itemId");
    const stockElement = document.getElementById("itemStock");
    const quantityInput = document.getElementById("quantity");
    const totalPriceElement = document.getElementById("totalPrice");
    const orderButton = document.getElementById("orderButton");
    const errorMessage = document.getElementById("errorMessage");

    const itemId = Number(itemIdElement.textContent);
    const stock = Number(stockElement.textContent);
    const price = Number(totalPriceElement.dataset.price);

    quantityInput.addEventListener("input", updateTotalPrice);
    orderButton.addEventListener("click", orderItem);

    function updateTotalPrice() {
        const quantity = Number(quantityInput.value);

        if (!Number.isInteger(quantity) || quantity < 1) {
            totalPriceElement.textContent = "0원";
            return;
        }

        totalPriceElement.textContent =
            `${(price * quantity).toLocaleString("ko-KR")}원`;
    }

    async function orderItem() {
        errorMessage.hidden = true;

        const quantity = Number(quantityInput.value);

        if (!Number.isInteger(quantity) || quantity < 1) {
            showError("주문 수량은 1개 이상이어야 합니다.");
            return;
        }

        if (quantity > stock) {
            showError("재고보다 많은 수량을 주문할 수 없습니다.");
            return;
        }

        orderButton.disabled = true;

        try {
            const response = await fetch("/api/orders", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    items: [
                        {
                            itemId: itemId,
                            quantity: quantity,
                            priceSnapshot: price
                        }
                    ]
                })
            });

            const result = await response.json();

            if (!response.ok || !result.isSuccess) {
                throw new Error(
                    result.message || "주문 처리에 실패했습니다."
                );
            }

            const orderId = result.data.orderId;

            if (orderId == null) {
                throw new Error("주문 번호를 확인할 수 없습니다.");
            }

            window.location.href = `/orders/${encodeURIComponent(orderId)}`;
        } catch (error) {
            showError(error.message || "주문 중 오류가 발생했습니다.");
        } finally {
            orderButton.disabled = false;
        }
    }

    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.hidden = false;
    }
});
document.addEventListener("DOMContentLoaded", loadItems);

async function loadItems() {
    const itemList = document.getElementById("item-list");
    const errorMessage = document.getElementById("error-message");

    try {
        console.log("상품 목록 조회 시작");

        const response = await fetch("/api/items");

        if (!response.ok) {
            throw new Error(`HTTP 오류: ${response.status}`);
        }

        const result = await response.json();

        console.log("API 응답:", result);

        if (!result.isSuccess) {
            throw new Error(result.message || "상품 목록 조회 실패");
        }

        const items = result.data.items;

        if (!Array.isArray(items)) {
            throw new Error("상품 목록 데이터가 배열이 아닙니다.");
        }

        renderItems(items, itemList);
    } catch (error) {
        console.error("상품 목록 조회 오류:", error);

        if (itemList) {
            itemList.replaceChildren();
        }

        if (errorMessage) {
            errorMessage.hidden = false;
            errorMessage.textContent =
                "상품 목록을 불러오지 못했습니다. 콘솔을 확인해 주세요.";
        }
    }
}

function renderItems(items, itemList) {
    itemList.replaceChildren();

    if (items.length === 0) {
        const message = document.createElement("p");
        message.className = "muted";
        message.textContent = "등록된 상품이 없습니다.";
        itemList.appendChild(message);
        return;
    }

    const fragment = document.createDocumentFragment();

    items.forEach(item => {
        const article = document.createElement("article");
        article.className = "card";

        const image = document.createElement("div");
        image.className = "placeholder-image";
        image.textContent = "ITEM";

        const content = document.createElement("div");
        content.className = "card-content";

        const title = document.createElement("h2");
        title.className = "card-title";
        title.textContent = item.name;

        const description = document.createElement("p");
        description.className = "muted";
        description.textContent = item.description ?? "";

        const price = document.createElement("p");
        price.className = "price";
        price.textContent = `${Number(item.price).toLocaleString("ko-KR")}원`;

        const link = document.createElement("a");
        link.className = "button";
        link.href = `/items/${encodeURIComponent(item.id)}`;
        link.textContent = "상품 상세 보기";

        content.append(title, description, price, link);
        article.append(image, content);
        fragment.appendChild(article);
    });

    itemList.appendChild(fragment);
}
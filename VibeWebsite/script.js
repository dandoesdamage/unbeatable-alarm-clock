// ==========================================================
// SoleStyle Store - Full Front-End Functionality
// ==========================================================

// 🛒 Add to Cart System
let cart = JSON.parse(localStorage.getItem("cart")) || [];
updateCartCount();

// Add to cart buttons (only on index.html)
const addCartButtons = document.querySelectorAll(".add-cart");
addCartButtons.forEach(btn => {
    btn.addEventListener("click", () => {
        const product = btn.closest(".product");
        const item = {
            id: product.dataset.id,
            name: product.dataset.name,
            price: parseFloat(product.dataset.price),
            img: product.dataset.img,
            qty: 1
        };

        // Check if already in cart
        const existing = cart.find(p => p.id === item.id);
        if (existing) {
            existing.qty++;
        } else {
            cart.push(item);
        }

        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartCount();
        alert(`✅ ${item.name} added to cart!`);
    });
});

// 🧮 Update cart count in nav
function updateCartCount() {
    const countEl = document.getElementById("cart-count");
    if (countEl) {
        const count = cart.reduce((sum, item) => sum + item.qty, 0);
        countEl.textContent = count;
    }
}

// 🧺 View Cart Page
if (document.getElementById("cart-items")) {
    const cartItemsDiv = document.getElementById("cart-items");
    const totalPriceEl = document.getElementById("total-price");

    function renderCart() {
        cartItemsDiv.innerHTML = "";
        let total = 0;

        if (cart.length === 0) {
            cartItemsDiv.innerHTML = "<p>Your cart is empty.</p>";
            totalPriceEl.textContent = "";
            return;
        }

        cart.forEach((item, index) => {
            const div = document.createElement("div");
            div.classList.add("cart-item");
            div.innerHTML = `
                <img src="${item.img}" alt="${item.name}">
                <div>
                    <h3>${item.name}</h3>
                    <p>₱${item.price.toLocaleString()}</p>
                    <p>Qty: <button onclick="changeQty(${index}, -1)">-</button> ${item.qty} <button onclick="changeQty(${index}, 1)">+</button></p>
                    <button onclick="removeItem(${index})">Remove</button>
                </div>
            `;
            cartItemsDiv.appendChild(div);
            total += item.price * item.qty;
        });

        totalPriceEl.textContent = `Total: ₱${total.toLocaleString()}`;
    }

    window.changeQty = (index, change) => {
        cart[index].qty += change;
        if (cart[index].qty <= 0) cart.splice(index, 1);
        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartCount();
        renderCart();
    };

    window.removeItem = (index) => {
        cart.splice(index, 1);
        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartCount();
        renderCart();
    };

    renderCart();

    // Checkout
    document.getElementById("checkout-btn").addEventListener("click", () => {
        if (cart.length === 0) {
            alert("Your cart is empty!");
            return;
        }
        alert("🧾 Checkout successful! Thank you for shopping!");
        cart = [];
        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartCount();
        renderCart();
    });
}

// 🔐 Login / Sign Up (localStorage-based)
if (document.getElementById("login-btn")) {
    const loginBtn = document.getElementById("login-btn");
    loginBtn.addEventListener("click", () => {
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!username || !password) {
            alert("Please enter username and password!");
            return;
        }

        let users = JSON.parse(localStorage.getItem("users")) || {};

        // If user doesn’t exist, create new one
        if (!users[username]) {
            users[username] = password;
            localStorage.setItem("users", JSON.stringify(users));
            alert("✅ Account created successfully!");
        } else {
            // Check credentials
            if (users[username] === password) {
                alert(`👋 Welcome back, ${username}!`);
                localStorage.setItem("currentUser", username);
                window.location.href = "index.html";
            } else {
                alert("❌ Incorrect password!");
            }
        }
    });
}

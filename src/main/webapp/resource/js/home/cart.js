var cartObj = JSON.parse(localStorage.getItem("cartObj"));
$(function () {

        let rs = "";
        if (cartObj == undefined) {

            rs += '<div class="alert alert-warning" role="alert">' +
                "Not thing in your cart!" +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                '           <span aria-hidden="true">&times;</span>' +
                "        </button>" +
                "</div>"
            $("#cartForm").before(rs);

        } else {
            for (let productIdKey in cartObj) {
                let cartObjI = cartObj[productIdKey];
                rs += `<tr class="col-12">
             <td class="col-4">
                 <img class="product-img"
                      src="${cartObjI.imageLink}"
                      alt=""/>
             </td>
             <td class="col-3"><span class="cart-item-price-span">${cartObjI.cartItemPrice}</span></td>
             <td class="col-3">
                 <input type="number" style="width: 30px; outline: none" value="${cartObjI.productCount}" min="1"/>
             </td>
             <td class="col-2"style="cursor: pointer">
                 <button style="font-weight: bold; color:orange; border : 1px solid orange; background-color: white">
                     Delete
                 </button>
             </td>
         </tr>`;
            }

            $("#cartTableBody").html(rs);


        }
        updateToTalPrice();
    }
)

function updateToTalPrice() {
    let total = 0;
    $(".cart-item-price-span").each(function () {
        let priceStr = $(this).text();
        let pricei = parseInt(priceStr);
        total += pricei;
    })
    $("#totalPriceSpan").text(total);
}

$("#checkoutBtn").on("click", function () {
    let data = {};
    data["price"] = parseInt($("#totalPriceSpan").text());
    let url = "/payment/redirect-vnpay-checkout"
    $.ajax({
        type: "post",
        url: url,
        data: data,
        success: function (redirecUrl) {
            window.location.href = redirecUrl;
        },
        error: function () {
            alert("Error occur")
        }
    });
});




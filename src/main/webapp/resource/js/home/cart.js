var cartObj = JSON.parse(localStorage.getItem("cartObj"));
console.log(cartObj)
$(function () {

        let rs = "";
        if (cartObj == undefined || Object.keys(cartObj).length === 0) {

            rs += '<div class="text-center">'
            rs += '<h2 style="color: #0a2ea4">Not thing in your cart!</h2> <br>'
            rs += '<a class="btn btn-info" href="/">Back to shop</a>'
            rs += '</div>'
            // $("#cartTable").css("display", "none");
            $("#cartForm").html(rs);

        } else {
            for (let productIdKey in cartObj) {
                let cartObjI = cartObj[productIdKey];

                let viewPrice = formatter.format(cartObjI.productPrice);
                let viewCartItemPrice = formatter.format(cartObjI.cartItemPrice);
                rs += `<tr id=${productIdKey} class="col-12">
             <td  class="col-4">
                 <img class="product-img"
                      src="${cartObjI.imageLink}"
                      alt=""/>
             </td>
             <td  class="col-2"><span id=${cartObjI.productPrice} class="product-price-span">${viewPrice}</span></td>
             <td class="col-2">
                 <input class="product-cnt-inp" type="number"  style="width: 45px; height: 45px; font-size:1.8rem; outline: none" value="${cartObjI.productCount}" min="1"/>
             </td>
             <td class="col-2">
            <span id=${cartObjI.cartItemPrice} class="cart-item-price-span">${viewCartItemPrice}</span>
            </td>
             <td class="col-2"style="cursor: pointer">
                 <button class="delCartItemBtn" style="font-weight: bold; color:orange; border : 1px solid orange; background-color: white">
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
        let cartItemPriceStr = $(this).prop('id');
        let cartItemPrice = parseInt(cartItemPriceStr);
        total += cartItemPrice;
    })
    $("#totalPriceSpan").parent().prop('id', total);

    $("#totalPriceSpan").text(formatter.format(total));
    $("#totalSpan").text(formatter.format(total));
}

$("#checkoutBtn").on("click", function () {
    let data = {};
    data["price"] = parseInt($("#totalPriceSpan").text());
    let url = "/payment/redirect-vnpay-checkout"
    ;


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

$(document).on("change", ".product-cnt-inp", function () {
    let trWrapper = $(this).parent().parent();
    let curId = $(trWrapper).prop('id');
    let cartItemPriceSpan = $(trWrapper).find(".cart-item-price-span");
    let productPriceSpan = $(trWrapper).find(".product-price-span");

    let newCartItemPrice = ($(this).val()) * parseInt($(productPriceSpan).prop('id'));
    cartObj[curId].cartItemPrice = newCartItemPrice;
    localStorage.setItem("cartObj", JSON.stringify(cartObj));


    $(cartItemPriceSpan).prop('id', cartObj[curId].cartItemPrice);
    $(cartItemPriceSpan).text(formatter.format(cartObj[curId].cartItemPrice));

})

$(document).on("blur", ".product-cnt-inp", function () {
    let trWrapper = $(this).parent().parent();
    let curId = $(trWrapper).prop('id');
    cartObj[curId].productCount = $(this).val();
    localStorage.setItem("cartObj", JSON.stringify(cartObj));
    updateToTalPrice();
    updateProductCounts();
    updateCartCountNumber();

})
$(document).on("click", ".delCartItemBtn", function () {
    let trWrapper = $(this).parent().parent();
    let curId = $(trWrapper).prop('id');
    delete cartObj[curId];

    localStorage.setItem("cartObj", JSON.stringify(cartObj));

    $(trWrapper).remove();

    updateToTalPrice();
    updateProductCounts();
    updateCartCountNumber();

})

function updateProductCounts() {
    let newProductCounts = 0;
    for (let cartItemKey in cartObj) {
        newProductCounts += parseInt(cartObj[cartItemKey].productCount);
    }
    localStorage.setItem("productCounts", String(newProductCounts));
}

$("#buyBtn").on("click", function () {
    document.getElementById("loader").style.display = "block";
    let url = "/transaction/add";
    $(".err-msg").each(function () {
        $(this).remove();

    })
    if (!validateUserForm("customerForm")) {
        return;
    }

    let orderItemList = [];
    let finalCartObj = JSON.parse(localStorage.getItem("cartObj"))
    for (let cartItemId in finalCartObj) {
        let curCartItem = finalCartObj[cartItemId];
        let orderi = {};
        orderi["product_id"] = curCartItem["productId"];
        orderi["unitPrice"] = curCartItem["productPrice"];
        orderi["qty"] = curCartItem["productCount"];

        orderi["options"] = curCartItem["color"];
        orderi["avatar"] = curCartItem["imageLink"];

        orderItemList.push(orderi);

    }
    let customerForm = $("#customerForm");
    let transaction = {}
    transaction["customerName"] = $(customerForm).find("input[name='username']").val()
    transaction["customerPhoneNumber"] = $(customerForm).find("input[name='phone']").val()
    transaction["customerAddress"] = $(customerForm).find("input[name='address']").val()
    transaction["customerGender"] = $(customerForm).find("input[name='gender']").val()
    transaction["totalPrice"] = $("#totalPriceSpan").parent().prop('id')

    let data = {};
    data["transaction"] = JSON.stringify(transaction);
    data["orderItemList"] = JSON.stringify(orderItemList);
    console.log(data);

    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json",

        success: function (res) {
            localStorage.removeItem("cartObj")
            localStorage.removeItem("productCounts");
            document.getElementById("loader").style.display = "none";
            if (localStorage.getItem("cartObj") == undefined) {

                if (confirm("Create new order with " + res + " successfully, go to order manage page") == true) {
                    window.location.href = "/transaction"
                } else {
                    window.location.href = "/";
                }
            }

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not order ',
                // text: 'Something went wrong!',

            })
            document.getElementById("loader").style.display = "none";
        }


    })
})


function validateUserForm(checkFormId) {
    let phoneno = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
    let checkForm = $("#" + checkFormId);

    let err = 0;
    if (!($(checkForm).find("input[name='username']").val().length > 0)) {
        err = 1;
        showAlertError(checkForm, "username", "Please check your name!");
    }
    if (!($(checkForm).find("input[name='phone']").val().match(phoneno))) {
        err = 1;
        showAlertError(checkForm, "phone", "Please check your phone number!");
    }

    if (!($(checkForm).find("input[name='address']").val().length > 0)) {
        err = 1;
        showAlertError(checkForm, "address", "Please check your address!");
    }
    if (err == 0) {
        return true;
    } else {
        return false;
    }

}

function showAlertError(checkForm, errInpName, errorMes) {
    let errAlert =
        `<div class="err-msg alert alert-warning alert-dismissible fade show" role="alert">
            <span>${errorMes}</span>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">×</span>
            </button>
        </div>`

    // let validateForm = document.getElementById(formId);
    let selector = "input[name='" + errInpName + "']";
    let failInp = $(checkForm).find(selector);

    $(failInp).after(errAlert);


}









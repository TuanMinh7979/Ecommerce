var imageColorLinkObj = {}

$(".pPriceSpan").text(formatter.format($(".pPriceSpan").prop('id')));
$(".pricespan").text(formatter.format($(".pricespan").prop('id')));
$(function () {
    getImageMap();
    ajaxGet(`/ajax/product/${productId}` + "/attributes", renderProductDetail);


})

// formatter.format(price)

function getImageMap() {
    $.ajax({
        type: "get",
        url: `/ajax/product/${productId}/image-color-link`,
        contentType: "application/json",
        success: function (colorLinkKV) {
            console.log(colorLinkKV);
            // imageColorLinkObj = JSON.parse(colorLinkKV);
            imageColorLinkObj = colorLinkKV;
            let links = [];
            links = Object.values(imageColorLinkObj);
            renderProductImagesSlide(links);

        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not load Filter UI",
                // text: 'Something went wrong!',
            });
        },
    });
}

function renderProductDetail(data) {
    // console.log(data);
    let container = $("#productSpec");
    let rs = "";
    let dataObj = {};
    dataObj = JSON.parse(data);

    for (let atri in dataObj) {
        rs += `<tr> <td>${atri}</td> <td>${dataObj[atri].value}</td></tr>`

    }


    container.html(rs);
}

function renderProductImagesSlide(data) {
    let container = $("#productDetailImageWrapper").find(".swiper-wrapper");
    let rs = "";
    data.map(function (datai) {

        rs += `<div class="swiper-slide"><img src="${datai}"/></div>`
    })
    container.html(rs);
    const productImagesSlide = createSomeSlideSwi("#productDetailImageWrapper");

}

var count = 1;
var pricei = $(".pPriceSpan").prop('id');

$("#buybtn").on("click", function () {
    renderImageToAddToCartModal();
    $("#showBuyModalBtn").click();
})

function renderImageToAddToCartModal() {
    let imagesSection = "";
    for (let colori in imageColorLinkObj) {
        if (!colori.startsWith("no")) {
            imagesSection += `<div><div><label>${colori}</label> <input type="radio" name="color" value="${colori}"></div>` +
                `<img style="height: 100px; width: 100px" class="item-img" src="${imageColorLinkObj[colori]}" alt=""/></div>`

        }
    }

    $("#colorChooseSection").html(imagesSection);
    $("input:radio[name='color']:first").attr('checked', true);

}

$("#upbtn").click(function (e) {
    e.preventDefault();
    count++;
    $("#countInp").val(count);
    console.log(pricei + "---" + count);
    $(".pPriceSpan").text(formatter.format(pricei * count));

});
$("#downbtn").click(function (e) {
    e.preventDefault();
    count--;
    if (count < 1) {
        count = 1;
    }
    $("#countInp").val(count);
    // $(".pPriceSpan").prop('id', pricei * count);
    $(".pPriceSpan").text(formatter.format(pricei * count));
});


$('#addToCartModalBody__Submit-Btn').click(function (e) {
    e.preventDefault();
    if (localStorage.getItem("productCounts") == undefined) {
        localStorage.setItem("productCounts", count);

    } else {
        localStorage.setItem("productCounts", parseInt(localStorage.getItem("productCounts")) + parseInt(count));
    }
    let addToCartModalBodySection = $(document).find("#addToCartModalBody");

    //
    let cartObjI = {};
    let selectedColorRadio = $(addToCartModalBodySection).find('input:radio[name="color"]:checked');

    cartObjI["productId"] = $("#pIdInp").val();
    cartObjI["color"] = $(selectedColorRadio).val();
    let choosedImg = $(selectedColorRadio).parent().parent().find(".item-img");
    cartObjI["imageLink"] = $(choosedImg).attr("src");

    cartObjI["productPrice"] = pricei;
    cartObjI["productCount"] = String(count);
    cartObjI["cartItemPrice"] = pricei * count;
    //
    let cartObjKey = $("#pIdInp").val();
    let cartObj = {};
    if (localStorage.getItem("cartObj") == undefined) {
        cartObj[cartObjKey] = cartObjI;
    } else {
        cartObj = JSON.parse(localStorage.getItem("cartObj"));
        cartObj[cartObjKey] = cartObjI;
    }
    let cartObjStr = JSON.stringify(cartObj);
    localStorage.setItem("cartObj", cartObjStr);

    $("#addToCartModal").modal('hide');
    alert("New product is added to cart!");
    updateCartCountNumber();
    // $("#homeCartCounter").html(localStorage.getItem("productCounts"));


});

function updateCartCountNumber() {
    $("#homeCartCounter").html(localStorage.getItem("productCounts") == undefined ? 0
        : localStorage.getItem("productCounts"));

}







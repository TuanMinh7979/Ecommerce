function getOrders() {
    let url = `/admin/transaction/${tranId}/orders`
    $.ajax({
        type: "get",
        url: url,
        success: function (data) {

            if (data.length != 0) {
                renderOrders(data);
            }

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not call this Api',
                // text: 'Something went wrong!',

            })
        }


    });

}

function renderOrders(data) {
    let rs = "";
    rs += ` <table id='orderTable'  class='col-12  table table-striped'>
         <thead class="col-12">
            <th class="col-2 ">Id</th>
            <th class="col-3 ">Avatar</th>
            <th class="col-2 ">Qty</th>
            <th class="col-2 ">UnitPrice</th>
            <th class="col-2">Action</th>
            </thead>`
    rs += "<tbody>"
    data.map(function (orderi) {
        rs += `<tr class="col-12">
<td class="col-2">${orderi.id}</td>
<td class="col-2">
<p class="order-product-name">${orderi.productName}</p>
<img style="height: 160px; width: 160px " src=${orderi.avatar} alt=""></td>
<td class="col-2">X${orderi.qty}</td>
<td class="col-2">${orderi.unitPrice}</td>
<td class="col-2"><a class="detailBtn btn btn-light">Detail</a>
</td>
  </tr>`


    })
    rs += "</tbody> </table>"
    $("#orderSection").html(rs);
}


function loadProcess() {
    let targetValue = $("#tStatusInp").val();
    if (targetValue == "FAILED") {
        $("#failedInp").prop("checked", true);
        currentStep = 0;
        return false
    }
    let allBullet = $(".bullet");
    $(allBullet).each(function () {
        $(this).addClass("completed");
        if ($(this).parent().find(".step-text").text() == targetValue) {
            currentStep = parseInt($(this).text());

            return false;
        }
    })
}

$("#failedInp").on('click', function () {
    if ($(this).prop('checked') == true && $("#tStatusInp").val() != 'FAILED') {
        $("#tStatusInp").val('FAILED');
    }

})

$('#cancelInProcessingCb').prop('checked', $('#cancelInProcessingCb').val());
$(document).on("click", ".detailBtn", function () {
    alert("This feature is in development!")
})

loadProcess();
getOrders();

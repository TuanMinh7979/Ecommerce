if (localStorage.getItem("customerPhoneNumber") == undefined) {

    let rs = "   <div class='text-center'><h3 style='color: blue'>You need enter phone number to show orders</h3></div>\n" +
        "    <div class='text-center'>" +
        "      <label>Your Ordered phone number</label>" +
        "      <input id='customerPhoneNumberInp' type='text' />" +
        "<button id='customerPhoneFormBtn' class='btn btn-info' style='margin-left: 60px'>GET</button>" +
        "    </div>";

    $("#transWrapper").html(rs);
} else {
    getTransactions(localStorage.getItem("customerPhoneNumber"));
}


$("#customerPhoneFormBtn").on("click", function () {
    let phoneNumber = $("#customerPhoneNumberInp").val();
    if (localStorage.getItem("customerPhoneNumber") == undefined) {
        localStorage.setItem("customerPhoneNumber", phoneNumber);
    }
    getTransactions(phoneNumber);
})


function getTransactions(phoneNumber) {
    $.ajax({
        type: "Post",
        url: "/ajax/client-transaction",
        data: JSON.stringify(phoneNumber),
        contentType: "application/json",

        success: function (res) {
            // if (document.getElementById("loader").style.display == "block") {
            //     document.getElementById("loader").style.display = "none";
            // }
            console.log(res);
            render(res);

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can get your transaction ',
                // text: 'Something went wrong!',

            })

        }


    })
}

function render(data) {
    let rs = "";
    console.log(data.length);
    rs += `     <table id="transTable" class="col-12 tableFixHeight table table-striped">
            <thead class="col-12">
            <th class="col-1 ">id</th>
            <th class="col-2 ">Name</th>
            <th class="col-2 ">Address</th>
            <th class="col-3">Products</th>
            <th class="col-2">Total price</th>
            <th class="col-2">Action</th>
            </thead>
            <tbody id="transTableBody">`
    data.map(function (trani) {

        rs += `<tr class="col-12" >
            <td class="col-1">${trani.id}</td>
            <td class="col-2">${trani.customerName}</td>
            <td class="col-2">${trani.customerAddress}</td>
            <td class="col-3">`
        trani.orderItemList.map(function (orderi) {
            rs += `
            <img src=${orderi.avatar} style="width:60px; height:60px" alt="">
            <span> X ${orderi.qty}</span>`

        })


        let clientPrice = formatter.format(trani.totalPrice);
        rs += `</td>
            <td id=${trani.totalPrice} class="col-2 price-td">${clientPrice}</td>
            <td class="col-2"><a style="margin-right: 30px; color: white" class='btn btn-success'>Pay</a><a style="color: white" class='btn btn-danger'>Cancel</a></td>
        </tr>`


    })
    rs += `</tbody>
        </table>`
    rs += ` <div><a id="otherPhoneNumberBtn" class="btn btn-outline-info">Other phone number</a></div>`

    $("#transWrapper").html(rs);

}

$(document).on("click", "#otherPhoneNumberBtn", function () {
    localStorage.removeItem("customerPhoneNumber");
    location.reload();

})
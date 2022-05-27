if (localStorage.getItem("customerPhoneNumber") == undefined) {

    let rs = "   <div class='text-center'><h3 style='color: blue'>You need enter phone number to show orders</h3></div>\n" +
        "    <div class='text-center'>" +
        "      <label>Your Ordered phone number</label>" +
        "      <input id='customerPhoneNumberInp' type='text' />" +
        "<button id='customerPhoneFormBtn' class='btn btn-info' style='margin-left: 60px'>GET</button>" +

        "    </div>" +
        "<div class='text-center'><a class='btn btn-info' href='/' style='color: white' >Back to shop</a> </div>"

    $("#transWrapper").html(rs);
// } else
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
            localStorage.setItem("transactionCount", res.length);
            updateTransactionCount();
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
    if (localStorage.getItem("transactionCount") == undefined || localStorage.getItem("transactionCount") == 0) {
        rs += `<h5 style="font-family: Arial; color: darkcyan; font-weight: bold">Logged Phone Number:${localStorage.getItem("customerPhoneNumber")}</h5>`
        rs += " <div class='text-center'><h3 style='color: orange'>There no any transaction now, let buy some thing!</h3></div>"
        rs += "<div class='row' style='display: flex; justify-content: space-around'>"
        rs += "<div class='float-left'><a class='btn btn-info' href='/' style='color: white' >Back to shop</a> </div>"
        rs += ` <div class='float-right' ><button id="otherPhoneNumberBtn" class=" other-phone-number-btn" >Other phone number</button></div>`

        rs += "</div>"
    } else {


        rs += rs += `<h5 style="font-family: Arial; color: darkcyan; font-weight: bold">Logged Phone Number:${localStorage.getItem("customerPhoneNumber")}</h5>
                 <table id="transTable" class="col-12 tableFixHeight table table-striped">
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
            <td id=${trani.id} class="col-1 id-td">${trani.id}</td>
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
            <td class="col-2"><a style="margin-right: 10px; color: white" class='payBtn btn btn-success'>Pay</a><a style="color: white" class='btn btn-danger trans-cancel-btn'>Cancel</a></td>
        </tr>`


        })
        rs += `</tbody>
        </table>`
        rs += "<div class='row' style='display: flex; justify-content: space-around'>"
        rs += "<div class='float-left'><a class='btn btn-info' href='/' style='color: white' >Back to shop</a> </div>"
        rs += ` <div class='float-right' ><button id="otherPhoneNumberBtn" class=" other-phone-number-btn" >Other phone number</button></div>`

        rs += "</div>"
    }
    $("#transWrapper").html(rs);


}

$(document).on("click", "#otherPhoneNumberBtn", function () {
    localStorage.removeItem("customerPhoneNumber");
    localStorage.removeItem("transactionCount");
    location.reload();

})

$(document).on("click", ".payBtn", function (event) {
    event.preventDefault();
    let data = {}
    let tranId = $(this).parent().parent().find('.id-td').prop('id');
    data["price"] = $(this).parent().parent().find('.price-td').prop('id');
    data["tranId"] = tranId;

    let url = `/payment/redirect-vnpay-checkout`
    // console.log(url);
    $.ajax({
        url: url,
        type: "post",
        data: data,
        success: function (response) {
            window.location.href = response;
        },
        error: function (err) {
            alert("Something wrong")
        }
    });


})

$(document).on("click", ".trans-cancel-btn", function () {

    let tranId = $(this).parent().parent().find('.id-td').prop('id');
    let url = `/ajax/client-transaction/cancel/${tranId}`
    $.ajax({
        url: url,
        type: "post",
        success: function (response) {
            alert("Cancel the transaction successfully!");
            location.reload();
        },
        error: function (err) {
            alert("Can not cancel this transaction!")
            location.reload()
        }
    });
})
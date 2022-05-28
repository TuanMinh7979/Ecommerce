function validateDatePicker(fromDate, toDate) {
    var fromDateObj = new Date(fromDate);
    var toDateObj = new Date(toDate);
    if (fromDateObj.getTime() >= toDateObj.getTime()) {
        return true;
    }
    return false;

}

function createLineChart(id, productTurnOverLabel = [], productTurnOverData = []) {


    const data = {
        labels: productTurnOverLabel,
        datasets: [{
            label: 'My First Dataset',
            data: productTurnOverData,
            backgroundColor: [
                'rgb(255, 99, 132)',
                'rgb(54, 162, 235)',
                'rgb(255, 205, 86)'
            ],
            hoverOffset: 4
        }]
    };
    const config = {
        type: 'bar',
        data: data,
    };
    let c = document.getElementById(id).getContext("2d");

    var curchar = new Chart(c, config)
}

$("#turnOverByProductBtn").on("click", function () {

    document.getElementById("loader").style.display = "block";
    let url = "/admin/stat/product-stat"


    callAndRender("myChart1", url);


})
$("#statDateViewBtn").on("click", function (event) {
    event.preventDefault();

    let afromDatepicker = $("#afromDatepicker").val();
    let atoDatepicker = $("#atoDatepicker").val();
    let url = $(this).prop('href');
    if (afromDatepicker != "") {
        url += "?fromDate=" + afromDatepicker;
    }
    if (atoDatepicker != "") {
        url += "&toDate=" + atoDatepicker;
    }
    if (url.indexOf("?") == -1) {
        url = url.replace("&", "?");
    }
    if (validateDatePicker(afromDatepicker, atoDatepicker)) {
        $("input[type=date]").val("")
        alert("Invalid choosed time, please choose again!");
    } else {
        document.getElementById("loader").style.display = "block";
        callAndRender("myChart1",url);
    }


})

function callAndRender(id, url) {
    $.ajax({
        type: "get",
        url: url,
        contentType: "application/json",
        success: function (data) {
            let labels = []
            let datas = []
            data.map(function (datai) {
                labels.push(datai[0]);
                datas.push(datai[1]);
            })
            if (datas.length == data.length) {
                console.log(labels);
                console.log(datas);
                createLineChart(id, labels, datas);
                document.getElementById("loader").style.display = "none";
            }

        },
        error: function (data) {
            document.getElementById("loader").style.display = "none";
        }


    })
}


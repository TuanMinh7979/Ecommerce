let sumPrice = $("#price-div .price-span").prop('id');
$("#price-div .price-span").text(formatter.format(sumPrice));

let transCount = localStorage.getItem("transactionCount");
transCount = (transCount - 1) >= 0 ? (transCount - 1) : 0
if (transCount > 0) {
    localStorage.setItem("transactionCount", transCount);
} else {
    localStorage.removeItem("transactionCount");
}
updateTransactionCount();
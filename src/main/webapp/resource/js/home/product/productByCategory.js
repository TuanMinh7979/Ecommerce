$(function () {
  loadFilterUI();
  loadSelectedTags();
  $("#select").on("change", function () {
    if ($(this).val() === "important") {
      $(this).addClass("red");
    }
  });
  $(".list-sort button").on("click", function (event) {
    event.preventDefault();
    $(".list-sort button").removeClass("active");
    $(this).toggleClass("active");
  });
});

//
function loadFilterUI() {
  let curUrl = window.location.href;
  let categoryId = curUrl.charAt(curUrl.lastIndexOf(".") + 1);
  $.ajax({
    type: "get",
    url: `/ajax/category/${categoryId}/filter`,
    contentType: "application/json",
    success: function (filterSet) {
      renderFilterUI(filterSet);
    },
    error: function () {
      Swal.fire({
        icon: "error",
        title: "Can not load content",
        // text: 'Something went wrong!',
      });
    },
  });
}

function renderFilterUI(filterSet) {
  let filterSetKObjs = JSON.parse(filterSet);

  $.ajax({
    type: "post",
    url: `/ajax/filter/ui-opt-name`,
    data: JSON.stringify(Object.keys(filterSetKObjs)),
    contentType: "application/json",
    success: function (data) {
      let kvAtbUiName = data;
      let rs = "";
      for (let filterKi in filterSetKObjs) {
        rs += `<div id=${filterKi} class="col-2 filter-option-key">
                <div class="dropdown">
                <button style="border :1px solid #D3D3D3"
                        class="btn dropdown-toggle"
                        type="button"
                        id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                
                ${kvAtbUiName[filterKi]}
                <!--ch-->
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">`;
        let optionKVs = filterSetKObjs[filterKi];

        for (let optionKi in optionKVs) {
          rs += `<a id="${optionKi}" onclick ="filterOptionValueClick(this, event)" class="dropdown-item filter-option-value"  href="#">${optionKVs[optionKi]}</a>`;
        }

        rs += `</div>
            </div>

            </div>`;
      }

      $("#listFilter").html(rs);
    },
    error: function () {
      Swal.fire({
        icon: "error",
        title: "Can not load content",
        // text: 'Something went wrong!',
      });
    },
  });
}

function filterOptionValueClick(thisAtag, event) {
  event.preventDefault();

  let namepar = $(thisAtag).closest(".filter-option-key").attr("id");
  let newParam = "&" + namepar + "=" + $(thisAtag).attr("id");

  let oldurl = window.location.href;
  let newurl = "";
  if (oldurl.indexOf(namepar) != -1) {
    let startIdx =
      oldurl.indexOf(namepar) +
      oldurl.substring(oldurl.indexOf(namepar)).indexOf("=");
    let endIdx = oldurl.indexOf(namepar);
    let endIdxFromName = oldurl.substring(endIdx).indexOf("&");
    let caseIsLastParName =
      endIdxFromName == -1
        ? oldurl.length - 1 - oldurl.indexOf(namepar)
        : endIdxFromName;
    endIdx += caseIsLastParName;

    if (endIdxFromName != -1) {
      newurl =
        oldurl.substring(0, startIdx + 1) +
        $(thisAtag).attr("id") +
        oldurl.substring(endIdx);
    } else {
      newurl = oldurl.substring(0, startIdx + 1) + $(thisAtag).attr("id");
    }
  } else {
    newurl = window.location.href + newParam;
  }

  let idxOfQuestionMark = newurl.indexOf("?");
  if (idxOfQuestionMark == -1) {
    let firstIdx = newurl.indexOf("&");
    newurl =
      newurl.substring(0, firstIdx) + "?" + newurl.substring(firstIdx + 1);
  }
  window.location.href = newurl;
}

function loadSelectedTags() {
  let params = new URL(document.location).searchParams;
  let keys = {};

  for (par of params.keys()) {
    keys[par] = params.get(par);
  }
  $.ajax({
    type: "post",
    url: "/ajax/filter/selected-tag-value",
    data: JSON.stringify(keys),
    contentType: "application/json",
    success: function (data) {
      let rs = "";
      data.map(function (filteri) {
        rs += `<div class="filter-tag">
        ${filteri.optionClientName} : ${filteri.optionValue}
        <button type="button" id ="${filteri.optionCode}" class="filter-tag-close close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
        </div>
        </div>`;

        $("#listFilterBy").html(rs);
      });
    },
    error: function () {
      Swal.fire({
        icon: "error",
        title: "Can not load selected tag",
        // text: 'Something went wrong!',
      });
    },
  });
}

$(document).on("click", ".filter-tag-close", function (event) {
  event.preventDefault();
  let paramValue = $(this).attr("id");

  let oldurl = window.location.href;
  let paramToDelIdx = oldurl.indexOf(paramValue);

  let subStr = oldurl.substring(0, paramToDelIdx);
  let tmpStartIdx1 = subStr.lastIndexOf("&");
  let tmpStartIdx2 = subStr.lastIndexOf("?");
  let startIdx;
  let endIdx;
  if (tmpStartIdx2 < tmpStartIdx1) {
    //tmp2 alway < tmp1 unless this is first param and tmp1 = -1
    //2 case interparam and lasparam
    startIdx = tmpStartIdx1;
  } else {
    startIdx = tmpStartIdx2;
  }
  let tmpEndIdx = startIdx + oldurl.substring(startIdx).indexOf("&");
  endIdx =
    oldurl.substring(startIdx + 1).indexOf("&") == -1
      ? oldurl.length
      : tmpEndIdx;

  let removePart = oldurl.substring(startIdx, endIdx);

  let newurl = oldurl.replace(removePart, "");

  if (newurl.indexOf("?") == -1) {
    newurl = newurl.replace("&", "?");
  }

  window.location.href = newurl;
});

function sortBtn(sortBy, sortDir) {
  let oldurl = window.location.href;
}

function addDelayedLinesFetch() {
    $(".js-hideable-row").each(function () {
        $(this).hide();
    });

    $(".js-clickable-row").on("click", function () {
        if ($(this).next(".js-hideable-row").is(":empty"))
        {
            var self = this;
            $.ajax({
                url: 'http://localhost:8080/FileStatViewer/webresources/com.evp.model.linestats/' + $(self).data("file-id"),
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    if (data && data.length > 0) {
                        var cur_row = $(self).next(".js-hideable-row");
                        cur_row.append('<td colspan="2"><table><tr><td>line_id</td><td>longestWord</td><td>shortestWord</td><td>lineLength</td><td>avgWordLength</td></tr></table></td>');
                        $.each(data, function (key, value) {
                            cur_row.find('tbody').append('<tr><td>' + value.lineId + '</td><td>' + value.longestWord + '</td><td>' + value.shortestWord + '</td><td>' + value.lineLength + '</td><td>' + value.avgWordLength + '</td></tr>');
                        });

                    }
                },
                error: function (xhr, textStatus, error) {
                    $('#main').append('<div class="error-msg">Error while getting data from server!</div>');
                }
            });
        }
        $(this).next(".js-hideable-row").toggle();
        $(this).toggleClass("active");
    });
}
function loadStats(count_lines) {
    $.ajax({
        url: 'http://localhost:8080/FileStatViewer/webresources/com.evp.model.filestats' + (count_lines !== undefined ? '/' + count_lines : ''),
        type: 'get',
        dataType: 'json',
        beforeSend: function () {
            $('#main').empty();
        },
        success: function (data) {
            if (data && data.length > 0) {
                $('#main').append('<table>');
                $('#main table').append('<tr><td>fileId</td><td>fileName</td></tr>');
                $.each(data, function (index, value) {
                    $('#main table').append('<tr class="js-clickable-row" data-file-id="' + value.fileId + '"><td>' + value.fileId + '</td><td>' + value.fileName + '</td></tr><tr class="js-hideable-row"></tr>');
                });
                addDelayedLinesFetch();
            } else {
                if (count_lines !== undefined) {
                    $('#main').append('<div class="error-msg">No files with such line count!</div>');
                }
            }
        },
        error: function (xhr, textStatus, error) {
            $('#main').append('<div class="error-msg">Error while getting data from server!</div>');
        }
    });
}
$(document).ready(function () {

    loadStats();

    $("#lineCountFilterSubmit").click(function () {
        var countLines = $("#lineCountFilter").val();
        if ($.isNumeric(countLines)) {
            loadStats(countLines);
        }
    });
});

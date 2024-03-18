
/**
 * @version 2024-03-18
 * @author Patrik Harag
 */

export function formatDate(date) {
    let dd = String(date.getDate()).padStart(2, '0');
    let MM = String(date.getMonth() + 1).padStart(2, '0');  // January is 0!
    let yyyy = date.getFullYear();

    let hh = String(date.getHours()).padStart(2, '0');
    let mm = String(date.getMinutes()).padStart(2, '0');

    return `${yyyy}-${MM}-${dd} ${hh}:${mm}`;
}

export function handle(alertOnSuccess, alertOnFailure, fetchPromise, onSuccess = undefined) {
    fetchPromise.then(r => {
        if (r.ok) {
            return r.text();
        } else {
            throw r.status
        }
    }).then(r => {
        if (alertOnSuccess) {
            alert('Success');
        }
        if (onSuccess !== undefined) {
            onSuccess(r);
        }
    }).catch(e => {
        if (alertOnFailure) {
            alert('Error: ' + e);
        }
    });
}


/**
 * @version 2024-02-10
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

SELECT park.name as parkname, campground.name as campgroundname, site.site_number, reservation.reservation_id, reservation.name as reservationname, reservation.from_date, reservation.to_date
FROM park
JOIN campground ON campground.park_id = park.park_id
JOIN site ON site.campground_id = campground.campground_id
JOIN reservation ON reservation.site_id = site.site_id
WHERE park.park_id = 1 AND from_date < '2020-03-05' AND to_date > '2020-02-23'
ORDER by from_date;
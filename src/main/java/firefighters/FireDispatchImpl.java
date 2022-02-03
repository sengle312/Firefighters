package firefighters;

import api.City;
import api.CityNode;
import api.FireDispatch;
import api.Firefighter;
import api.exceptions.NoFireFoundException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

public class FireDispatchImpl implements FireDispatch {
    private int numFirefighters;
    private City city;
    private List<CityNode> burningBuildings = new ArrayList<>();
    private List<Firefighter> firefighters = new ArrayList<>();
    private CityNode firehouseLocation;

    public FireDispatchImpl(City city) {
        this.city = city;
    }

    public void getFirefighterLocation() {

    }

    @Override
    public void setFirefighters(int numFirefighters) {
        for (int i = 0; i < numFirefighters; i++) {
            this.getFirefighters().add(new FirefighterImpl(city.getFireStation().getLocation()));
        }
    }

    @Override
    public List<Firefighter> getFirefighters() {
        return firefighters;
    }

    public int getNumFirefighters() {
        return numFirefighters;
    }

    public void setNumFirefighters(int numFirefighters) {
        this.numFirefighters = numFirefighters;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<CityNode> getBurningBuildings() {
        return burningBuildings;
    }

    public void setBurningBuildings(List<CityNode> burningBuildings) {
        this.burningBuildings = burningBuildings;
    }

    @Override
    public void dispatchFirefighers(CityNode... burningBuildings) {
        List<CityNode> destinations = new ArrayList<>(Arrays.asList(burningBuildings));

        List<ImmutablePair<List<CityNode>, Integer>> possibleRoutes = getPossibleRoutes(destinations);

        // Order the permutations by lowest total distance and choose the first route in the list
        List<CityNode> bestRoute = possibleRoutes
                .stream()
                .sorted(Comparator.comparing(ImmutablePair::getRight))
                .collect(Collectors.toList())
                .get(0).getLeft();

        // remove the firestation from the route list because we will split up the route list amongst the firefighters
        // and their routes will start from their current locations
        bestRoute.remove(0);

        // get the number of stops each firefighter should have to equally divide the routes
        // if the route size is not equally split, we increase the route size by 1 and the last firefighter will have
        // fewer stops that the rest
        int routeSize = bestRoute.size() % firefighters.size() == 0
                ? bestRoute.size() / firefighters.size()
                : bestRoute.size() / firefighters.size() + 1;

        // sequentially split the route into multiple routes.
        // Ex. 3 firefighters and 6 fires: Firefighter 1 will be assigned [0,1], Firefighter 2 will be assigned [2,3].
        // Firefighter 3 will be assigned [4,5]
        List<List<CityNode>> routes = Lists.partition(bestRoute, routeSize);

        for (int f = 0; f < routes.size(); f++) {
            List<CityNode> firefighterRoute = routes.get(f);
            Firefighter firefighterOnScene = firefighters.get(f);
            LinkedList<CityNode> remainingBuildingsInRoute = new LinkedList<>(firefighterRoute);
            for (int r = 0; r < firefighterRoute.size(); r++) {
                if (remainingBuildingsInRoute.size() == 0) {
                    break;
                }
                CityNode nextBuildingToExtinguish = firefighterRoute.get(r);
                CityNode currentLocation = firefighterOnScene.getLocation();
                try {
                    sendFirefighterToBuilding(firefighterOnScene, currentLocation, nextBuildingToExtinguish);
                } catch (NoFireFoundException nff) {
                    System.out.println(nff.getMessage());
                    // send firefighter to next location
                } finally {
                    remainingBuildingsInRoute.remove(nextBuildingToExtinguish);
                }
            }
        }
    }

    private void sendFirefighterToBuilding(Firefighter firefighter, CityNode startingLocation, CityNode nextLocation) throws NoFireFoundException {
        firefighter.addDistanceTraveled(getDistanceBetweenBuildings(startingLocation, nextLocation));
        firefighter.setLocation(nextLocation);
        city.getBuilding(nextLocation).extinguishFire();
    }

    private static Integer getDistanceBetweenBuildings(CityNode start, CityNode end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }

    private List<ImmutablePair<List<CityNode>, Integer>> getPossibleRoutes(List<CityNode> stops) {
        return getPossibleRoutes(new ArrayList<>(), new ArrayList<>(), stops);
    }

    private List<ImmutablePair<List<CityNode>, Integer>> getPossibleRoutes(List<ImmutablePair<List<CityNode>, Integer>> allPossibleRoutes, List<CityNode> currentPossibleRoutes, List<CityNode> buildingsOnFire) {
        if (allPossibleRoutes == null) {
            allPossibleRoutes = new LinkedList<>();
        }
        if (currentPossibleRoutes.size() == buildingsOnFire.size()) {
            List<CityNode> permsWithFirestation = new ArrayList<>(currentPossibleRoutes);
            // always add the firestation to the route as the starting place
            permsWithFirestation.add(0, city.getFireStation().getLocation());
            int totalDistance = 0;
            // want to stop the loop before the last building  because there won't be any more buildings to travel to
            for (int i = 0; i < permsWithFirestation.size() - 1; i++) {
                totalDistance += getDistanceBetweenBuildings(permsWithFirestation.get(i), permsWithFirestation.get(i + 1));
            }
            allPossibleRoutes.add(new ImmutablePair<>(new ArrayList<>(permsWithFirestation), totalDistance));
        } else {
            for (int i = 0; i < buildingsOnFire.size(); i++) {

                // stop duplicates
                if (currentPossibleRoutes.contains(buildingsOnFire.get(i))) {
                    continue;
                }
                currentPossibleRoutes.add(buildingsOnFire.get(i));
                getPossibleRoutes(allPossibleRoutes, currentPossibleRoutes, buildingsOnFire);
                currentPossibleRoutes.remove(currentPossibleRoutes.size() - 1);
            }
        }
        return allPossibleRoutes;
    }
}

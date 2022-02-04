package scenarios;

import api.*;
import api.exceptions.FireproofBuildingException;
import impls.CityImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BasicScenarios {
    @Test
    public void singleFire() throws FireproofBuildingException {
        City basicCity = new CityImpl(5, 5, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();

        CityNode fireNode = new CityNode(0, 1);
        Pyromaniac.setFire(basicCity, fireNode);

        fireDispatch.setFirefighters(1);
        fireDispatch.dispatchFirefighers(fireNode);
        Assert.assertFalse(basicCity.getBuilding(fireNode).isBurning());
    }

    @Test
    public void singleFireDistanceTraveledDiagonal() throws FireproofBuildingException {
        City basicCity = new CityImpl(2, 2, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();

        // Set fire on opposite corner from Fire Station
        CityNode fireNode = new CityNode(1, 1);
        Pyromaniac.setFire(basicCity, fireNode);

        fireDispatch.setFirefighters(1);
        fireDispatch.dispatchFirefighers(fireNode);

        Firefighter firefighter = fireDispatch.getFirefighters().get(0);
        Assert.assertEquals(2, firefighter.distanceTraveled());
        Assert.assertEquals(fireNode, firefighter.getLocation());
    }

    @Test
    public void singleFireDistanceTraveledAdjacent() throws FireproofBuildingException {
        City basicCity = new CityImpl(2, 2, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();

        // Set fire on adjacent X position from Fire Station
        CityNode fireNode = new CityNode(1, 0);
        Pyromaniac.setFire(basicCity, fireNode);

        fireDispatch.setFirefighters(1);
        fireDispatch.dispatchFirefighers(fireNode);

        Firefighter firefighter = fireDispatch.getFirefighters().get(0);
        Assert.assertEquals(1, firefighter.distanceTraveled());
        Assert.assertEquals(fireNode, firefighter.getLocation());
    }

    @Test
    public void simpleDoubleFire() throws FireproofBuildingException {
        City basicCity = new CityImpl(2, 2, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();


        CityNode[] fireNodes = {
                new CityNode(0, 1),
                new CityNode(1, 1)};
        Pyromaniac.setFires(basicCity, fireNodes);

        fireDispatch.setFirefighters(1);
        fireDispatch.dispatchFirefighers(fireNodes);

        Firefighter firefighter = fireDispatch.getFirefighters().get(0);
        Assert.assertEquals(2, firefighter.distanceTraveled());
        Assert.assertEquals(fireNodes[1], firefighter.getLocation());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    }

    @Test
    public void simpleQuadFire() throws FireproofBuildingException {
        City basicCity = new CityImpl(3, 3, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();


        CityNode[] fireNodes = {
                new CityNode(0, 1),
                new CityNode(1, 1),
                new CityNode(2, 2),
                new CityNode(2, 1)};
        Pyromaniac.setFires(basicCity, fireNodes);

        fireDispatch.setFirefighters(1);
        fireDispatch.dispatchFirefighers(fireNodes);

        Firefighter firefighter = fireDispatch.getFirefighters().get(0);
        Assert.assertEquals(4, firefighter.distanceTraveled());
        Assert.assertEquals(fireNodes[2], firefighter.getLocation());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[2]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[3]).isBurning());
    }

    @Test
    public void doubleFirefighterDoubleFire() throws FireproofBuildingException {
        City basicCity = new CityImpl(2, 2, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();


        CityNode[] fireNodes = {
                new CityNode(0, 1),
                new CityNode(1, 0)};
        Pyromaniac.setFires(basicCity, fireNodes);

        fireDispatch.setFirefighters(2);
        fireDispatch.dispatchFirefighers(fireNodes);

        List<Firefighter> firefighters = fireDispatch.getFirefighters();
        int totalDistanceTraveled = 0;
        boolean firefighterPresentAtFireOne = false;
        boolean firefighterPresentAtFireTwo = false;
        for (Firefighter firefighter : firefighters) {
            totalDistanceTraveled += firefighter.distanceTraveled();

            if (firefighter.getLocation().equals(fireNodes[0])) {
                firefighterPresentAtFireOne = true;
            }
            if (firefighter.getLocation().equals(fireNodes[1])) {
                firefighterPresentAtFireTwo = true;
            }
        }

        Assert.assertEquals(2, totalDistanceTraveled);
        Assert.assertTrue(firefighterPresentAtFireOne);
        Assert.assertTrue(firefighterPresentAtFireTwo);
        Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    }

    @Test
    public void doubleFirefighterFiveFires() throws FireproofBuildingException {
        City basicCity = new CityImpl(4, 4, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();


        CityNode[] fireNodes = {
                new CityNode(0, 1),
                new CityNode(1, 1),
                new CityNode(2, 2),
                new CityNode(2, 1),
                new CityNode(2, 3)};
        Pyromaniac.setFires(basicCity, fireNodes);

        fireDispatch.setFirefighters(2);
        fireDispatch.dispatchFirefighers(fireNodes);

        List<Firefighter> firefighters = fireDispatch.getFirefighters();
        int totalDistanceTraveled = 0;
        boolean firefighterPresentAtFireFour = false;
        boolean firefighterPresentAtFireFive = false;
        for (Firefighter firefighter : firefighters) {
            totalDistanceTraveled += firefighter.distanceTraveled();

            if (firefighter.getLocation().equals(fireNodes[3])) {
                firefighterPresentAtFireFour = true;
            }
            if (firefighter.getLocation().equals(fireNodes[4])) {
                firefighterPresentAtFireFive = true;
            }
        }

        Assert.assertEquals(8, totalDistanceTraveled);
        Assert.assertTrue(firefighterPresentAtFireFour);
        Assert.assertTrue(firefighterPresentAtFireFive);
        Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[2]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[3]).isBurning());
        Assert.assertFalse(basicCity.getBuilding(fireNodes[4]).isBurning());
    }

    @Test
    public void testGetRoutesDividesProperly() throws FireproofBuildingException {
        City basicCity = new CityImpl(10, 10, new CityNode(0, 0));
        FireDispatch fireDispatch = basicCity.getFireDispatch();

        CityNode[] fireNodes = {
                new CityNode(0, 1),
                new CityNode(0, 2),
                new CityNode(0, 3),
                new CityNode(0, 4),
                new CityNode(1, 1),
                new CityNode(1, 2),
                new CityNode(1, 3)};
        Pyromaniac.setFires(basicCity, fireNodes);

        CityNode[] bestRoute = {
                fireNodes[0],
                fireNodes[1],
                fireNodes[2],
                fireNodes[3],
                fireNodes[6],
                fireNodes[5],
                fireNodes[4]
        };
        fireDispatch.setFirefighters(3);

        List<List<CityNode>> routes = fireDispatch.getRoutes(Arrays.asList(bestRoute));

        Assert.assertEquals(routes.get(0).size(), 3);
        Assert.assertEquals(routes.get(1).size(), 2);
        Assert.assertEquals(routes.get(2).size(), 2);
    }
}

package api;

public interface Firefighter {

  /**
   * Get the firefighter's current location. Initially, the firefighter should be at the FireStation
   *
   * @return {@link CityNode} representing the firefighter's current location
   */
  CityNode getLocation();

  /**
   * Set current location of the firefighter
   * @param location
   */
  void setLocation(CityNode location);

  /**
   * Add distanced traveled to the total distance traveled of the firefighter
   * @param distance
   */
  void addDistanceTraveled(int distance);

  /**
   * Get the total distance traveled by this firefighter. Distances should be represented using TaxiCab
   * Geometry: https://en.wikipedia.org/wiki/Taxicab_geometry
   *
   * @return the total distance traveled by this firefighter
   */
  int distanceTraveled();


}
